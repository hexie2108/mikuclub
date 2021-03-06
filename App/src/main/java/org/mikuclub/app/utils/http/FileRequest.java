package org.mikuclub.app.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

/**
 * Custom request to make multipart header and upload file.
 * Sketch Project Studio
 * Created by Angga on 27/04/2016 12.05.
 * source :https://gist.github.com/anggadarkprince/a7c536da091f4b26bb4abf2f92926594
 */
public class FileRequest extends Request<NetworkResponse>
{
        private final String twoHyphens = "--";
        private final String lineEnd = "\r\n";
        private final String boundary = "apiclient-" + System.currentTimeMillis();

        private static final String CONTENT_TYPE_JSON = "application/json";
        private static final String CONTENT_TYPE_TEXT = "text/plain";

        private Response.Listener<NetworkResponse> mListener;
        private Response.ErrorListener mErrorListener;
        private Map<String, String> mHeaders;
        private Map<String, String> mParams;
        private File mFile;

        /**
         * Default constructor with predefined header and post method.
         *
         * @param url           request destination
         * @param bodyParams        params of request
         * @param file          file to upload
         * @param headers       headers of request
         * @param listener      on success achieved 200 code from request
         * @param errorListener on error http or library timeout
         */
        public FileRequest(int method, String url, Map<String, String> bodyParams, File file, Map<String, String> headers,
                           Response.Listener<NetworkResponse> listener,
                           Response.ErrorListener errorListener)
        {
                super(method, url, errorListener);
                this.mListener = listener;
                this.mErrorListener = errorListener;
                this.mHeaders = headers;
                this.mParams = bodyParams;
                this.mFile = file;
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
                if (mHeaders != null && mHeaders.size() > 0)
                {
                        return mHeaders;
                }
                else
                {
                        return Collections.emptyMap();
                }
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
                if (mParams != null && mParams.size() > 0)
                {
                        return mParams;
                }
                return super.getParams();

        }

        /**
         * 弃用
         * 支持解析 body里的对象参数
         *
         * @return Map data part label with data byte
         * @throws IOException
         */

        /*
        private Map<String, DataPart> getByteDataOfBodyParameters()
        {
                Map<String, DataPart> params = new HashMap<>();

                for (Map.Entry<String, Object> entry : mParams.entrySet())
                {
                        byte[] byteData;
                        String contentType;
                        if (entry.getValue() instanceof String)
                        {
                                byteData = ((String) entry.getValue()).getBytes(StandardCharsets.UTF_8);
                                contentType = CONTENT_TYPE_TEXT;
                        }
                        else
                        {
                                byteData = ParserUtils.toJson(entry.getValue()).getBytes();
                                contentType = CONTENT_TYPE_JSON;
                        }
                        params.put(entry.getKey(), new DataPart(mFile.getName(), byteData, contentType));
                }


                return params;
        }
        */



        @Override
        public byte[] getBody() throws AuthFailureError
        {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                try
                {
                        // populate text payload
                       Map<String, String> params = getParams();
                        if (params != null && params.size() > 0)
                        {
                                textParse(dos, params, getParamsEncoding());
                        }

                        // populate data byte payload
                        //设置body参数
                      //  getByteDataOfBodyParameters();
                        //设置body文件
                        Map<String, DataPart> data =  getByteData();
                        if (data != null && data.size() > 0)
                        {
                                dataParse(dos, data);
                        }

                        // close multipart form data after text and file data
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        return bos.toByteArray();
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
                return null;
        }


        @Override
        public String getBodyContentType()
        {
                return "multipart/form-data;boundary=" + boundary;
        }


        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response)
        {
                try
                {
                        return Response.success(
                                response,
                                HttpHeaderParser.parseCacheHeaders(response));
                }
                catch (Exception e)
                {
                        return Response.error(new ParseError(e));
                }
        }

        @Override
        protected void deliverResponse(NetworkResponse response)
        {
                mListener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error)
        {
                mErrorListener.onErrorResponse(error);
        }

        /**
         * method handle data payload.
         *
         * @return Map data part label with data byte
         * @throws IOException
         */

        private Map<String, DataPart> getByteData() throws IOException
        {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                byte[] fileContent = FileUtils.readFileToByteArray(mFile);

                params.put("file", new DataPart(mFile.getName(), fileContent, new MimetypesFileTypeMap().getContentType(mFile)));

                return params;
        }


        /**
         * Parse string map into data output stream by key and value.
         *
         * @param dataOutputStream data output stream handle string parsing
         * @param params           string inputs collection
         * @param encoding         encode the inputs, default UTF-8
         * @throws IOException
         */
        private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException
        {
                try
                {
                        for (Map.Entry<String, String> entry : params.entrySet())
                        {
                                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
                        }
                }
                catch (UnsupportedEncodingException uee)
                {
                        throw new RuntimeException("Encoding not supported: " + encoding, uee);
                }
        }

        /**
         * Parse data into data output stream.
         *
         * @param dataOutputStream data output stream handle file attachment
         * @param data             loop through data
         * @throws IOException
         */
        private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException
        {
                for (Map.Entry<String, DataPart> entry : data.entrySet())
                {
                        buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
                }
        }

        /**
         * Write string data into header and data output stream.
         *
         * @param dataOutputStream data output stream handle string parsing
         * @param parameterName    name of input
         * @param parameterValue   value of input
         * @throws IOException
         */
        private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException
        {
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
                //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(parameterValue + lineEnd);
        }

        /**
         * Write data file into header and data output stream.
         *
         * @param dataOutputStream data output stream handle data parsing
         * @param dataFile         data byte as DataPart from collection
         * @param inputName        name of data input
         * @throws IOException
         */
        private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException
        {
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                        inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
                if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty())
                {
                        dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
                }
                dataOutputStream.writeBytes(lineEnd);

                ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
                int bytesAvailable = fileInputStream.available();

                int maxBufferSize = 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
        }

        /**
         * Simple data container use for passing byte file
         */
        public class DataPart
        {
                private String fileName;
                private byte[] content;
                private String type;

                /**
                 * Default data part
                 */
                public DataPart()
                {
                }

                /**
                 * Constructor with data.
                 *
                 * @param name label of data
                 * @param data byte data
                 */
                public DataPart(String name, byte[] data)
                {
                        fileName = name;
                        content = data;
                }

                /**
                 * Constructor with mime data type.
                 *
                 * @param name     label of data
                 * @param data     byte data
                 * @param mimeType mime data like "image/jpeg"
                 */
                public DataPart(String name, byte[] data, String mimeType)
                {
                        fileName = name;
                        content = data;
                        type = mimeType;
                }

                /**
                 * Getter file name.
                 *
                 * @return file name
                 */
                public String getFileName()
                {
                        return fileName;
                }

                /**
                 * Setter file name.
                 *
                 * @param fileName string file name
                 */
                public void setFileName(String fileName)
                {
                        this.fileName = fileName;
                }

                /**
                 * Getter content.
                 *
                 * @return byte file data
                 */
                public byte[] getContent()
                {
                        return content;
                }

                /**
                 * Setter content.
                 *
                 * @param content byte file data
                 */
                public void setContent(byte[] content)
                {
                        this.content = content;
                }

                /**
                 * Getter mime type.
                 *
                 * @return mime type
                 */
                public String getType()
                {
                        return type;
                }

                /**
                 * Setter mime type.
                 *
                 * @param type mime type
                 */
                public void setType(String type)
                {
                        this.type = type;
                }
        }
}