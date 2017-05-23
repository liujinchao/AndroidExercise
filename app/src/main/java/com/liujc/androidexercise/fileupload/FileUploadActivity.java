package com.liujc.androidexercise.fileupload;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liujc.androidexercise.R;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * 类名称：FileUploadActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/5/17 14:33
 * 描述：TODO
 * 最近修改时间：2017/5/17 14:33
 * 修改人：Modify by liujc
 */
public class FileUploadActivity extends AppCompatActivity  implements View.OnClickListener{
    private EditText edit_fname;
    private Button btn_upload;
    private Button btn_stop;
    private ProgressBar pgbar;
    private TextView txt_result;

    private UploadHelper upHelper;
    private boolean flag = true;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pgbar.setProgress(msg.getData().getInt("length"));
            float num = (float) pgbar.getProgress() / (float) pgbar.getMax();
            int result = (int) (num * 100);
            txt_result.setText(result + "%");
            if (pgbar.getProgress() == pgbar.getMax()) {
                Toast.makeText(FileUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        bindViews();
        upHelper = new UploadHelper(this);
    }

    private void bindViews() {
        edit_fname = (EditText) findViewById(R.id.edit_fname);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        pgbar = (ProgressBar) findViewById(R.id.pgbar);
        txt_result = (TextView) findViewById(R.id.txt_result);

        btn_upload.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                flag = true;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.btn_stop:
                flag = false;
                break;
        }
    }

    //通过socket上传图片
    private void uploadFile(final File file) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String sourceid = upHelper.getBindId(file);
                    Socket socket = new Socket("192.168.1.52", 12345);
                    OutputStream outStream = socket.getOutputStream();
                    String head = "Content-Length=" + file.length() + ";filename=" + file.getName()
                            + ";sourceid=" + (sourceid != null ? sourceid : "") + "\r\n";
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String responseSourceid = items[0].substring(items[0].indexOf("=") + 1);
                    String position = items[1].substring(items[1].indexOf("=") + 1);
                    if (sourceid == null) {//如果是第一次上传文件，在数据库中不存在该文件所绑定的资源id
                        upHelper.save(responseSourceid, file);
                    }
                    RandomAccessFile fileOutStream = new RandomAccessFile(file, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while (flag && (len = fileOutStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        length += len;//累加已经上传的数据长度
                        Message msg = new Message();
                        msg.getData().putInt("length", length);
                        handler.sendMessage(msg);
                    }
                    if (length == file.length()) upHelper.delete(file);
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                } catch (Exception e) {
                    Toast.makeText(FileUploadActivity.this, "上传异常~", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    //通过servlet上传图片
    private void upLoadByHttp(final File file){
        final String path = "http://192.168.1.52:8080/UploadShipServlet";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String requst = UploadUtil.uploadFile(file,path);
                Log.d("TAG",requst);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //获取图片路径
            Uri selectedImage = data.getData();
            String imagePath = getPathByUri(FileUploadActivity.this,selectedImage);
            File file = new File(imagePath);
            if (file.exists()) {
                Toast.makeText(this, file.length()+"", Toast.LENGTH_SHORT).show();
                pgbar.setMax((int) file.length());
//                uploadFile(file);//socket通信传输
                upLoadByHttp(file);//servlet传输
            } else {
                Toast.makeText(FileUploadActivity.this, "文件并不存在~", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPathByUri(Context context, Uri data){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathByUri4BeforeKitkat(context, data);
        }else {
            return getPathByUri4AfterKitkat(context, data);
        }
    }
    //4.4以前通过Uri获取路径：data是Uri，filename是一个String的字符串，用来保存路径
    public static String getPathByUri4BeforeKitkat(Context context, Uri data) {
        String filename=null;
        if (data.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(data, new String[] { "_data" }, null, null, null);
            if (cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
        } else if (data.getScheme().toString().compareTo("file") == 0) {// file:///开头的uri
            filename = data.toString().replace("file://", "");// 替换file://
            if (!filename.startsWith("/mnt")) {// 加上"/mnt"头
                filename += "/mnt";
            }
        }
        return filename;
    }
    //4.4以后根据Uri获取路径：
    @SuppressLint("NewApi")
    public static String getPathByUri4AfterKitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
