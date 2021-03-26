package com.yumik.chickenneckblog.utils.downLoad

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.yumik.chickenneckblog.ProjectApplication
import com.yumik.chickenneckblog.logic.network.DownLoadNetwork
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.util.*
import java.util.concurrent.Executors


class DownloadUtil {

    companion object {
        private const val BUFFER_SIZE = 1024 * 4
    }

    fun download(url: String, path: String, downloadListener: DownloadListener) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ProjectApplication.BASE_URL)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()

        val service = retrofit.create(DownLoadNetwork::class.java)
        val call = service.download(url)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                writeResponseToDisk(path, response, downloadListener);
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                downloadListener.onFail("无法连接到网络");
            }

        })
    }

    private fun writeResponseToDisk(
        path: String,
        response: Response<ResponseBody>,
        downloadListener: DownloadListener
    ) {
        //从response获取输入流以及总大小
        writeFileFromIS(
            File(path),
            response.body()!!.byteStream(),
            response.body()!!.contentLength(),
            downloadListener
        )
    }

    private fun writeFileFromIS(
        file: File,
        inputStream: InputStream,
        totalLength: Long,
        downloadListener: DownloadListener
    ) {
        downloadListener.onStart()
        if (!file.exists()) {
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdir()
            }
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                downloadListener.onFail(e.message)
            }
        }

        var outputStream: OutputStream? = null
        var currentLength = 0
//        var startTime = Date().time
        try {
            outputStream = BufferedOutputStream(FileOutputStream(file))
            val data = ByteArray(BUFFER_SIZE)
            var len: Int
            while (inputStream.read(data, 0, BUFFER_SIZE).also { len = it } != -1) {
                outputStream.write(data, 0, len)
                currentLength += len
                downloadListener.onProgress(
                    (100 * currentLength / totalLength).toInt(),
                    currentLength,
                    totalLength
                )
            }
            downloadListener.onFinish(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            downloadListener.onFail(e.message)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun installApp(context: Context, fileProvider: String, apkPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                fileProvider, File(apkPath)
            )
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(
                Uri.fromFile(File(apkPath)),
                "application/vnd.android.package-archive"
            )
        }
        context.startActivity(intent)
    }
}