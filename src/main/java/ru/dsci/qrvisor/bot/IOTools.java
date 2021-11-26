package ru.dsci.qrvisor.bot;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class IOTools {

    private final static int BUFFER_SIZE = 65535;
    private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(readFileFromUrl(url));
        return jsonObject.getJSONObject("result");
    }

    private static String readFileFromUrl(String url) throws IOException, JSONException {
        try (ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream())) {
            ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
            channel.read(buff);
            return new String(buff.array(), DEFAULT_CHARSET);
        } catch (RuntimeException e) {
            throw new IOException(String.format("Error reading resource '%s': %s", url, e.getMessage()));
        }
    }
}
