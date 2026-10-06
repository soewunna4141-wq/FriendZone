package com.fz.friendzone.core.media

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

class CameraXCaptureManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val executor: Executor = ContextCompat.getMainExecutor(context)
) : CameraCaptureManager {

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    fun startCamera(
        onReady: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                val imageCaptureUseCase = ImageCapture.Builder()
                    .build()

                val qualitySelector =
                    QualitySelector.fromOrderedList(
                        listOf(
                            Quality.FHD,
                            Quality.HD,
                            Quality.SD
                        )
                    )

                val recorder = Recorder.Builder()
                    .setQualitySelector(qualitySelector)
                    .build()

                val videoCaptureUseCase =
                    VideoCapture.withOutput(recorder)

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCaptureUseCase,
                    videoCaptureUseCase
                )

                imageCapture = imageCaptureUseCase
                videoCapture = videoCaptureUseCase

                onReady()
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }, executor)
    }

    override fun captureImage(
        onResult: (Uri?) -> Unit
    ) {
        val capture = imageCapture ?: run {
            onResult(null)
            return
        }

        val outputDirectory =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: context.filesDir

        val file = File(
            outputDirectory,
            createFileName("jpg")
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(file)
                .build()

        capture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    onResult(Uri.fromFile(file))
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {
                    onResult(null)
                }
            }
        )
    }

    override fun startVideoCapture(
        onResult: (Uri?) -> Unit
    ) {
        val capture = videoCapture ?: run {
            onResult(null)
            return
        }

        if (recording != null) {
            onResult(null)
            return
        }

        val outputDirectory =
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                ?: context.filesDir

        val file = File(
            outputDirectory,
            createFileName("mp4")
        )

        val outputOptions =
            FileOutputOptions.Builder(file)
                .build()

        recording = capture.output
            .prepareRecording(
                context,
                outputOptions
            )
            .withAudioEnabled()
            .start(executor) { event ->
                when (event) {
                    is androidx.camera.video.VideoRecordEvent.Finalize -> {
                        recording = null

                        if (!event.hasError()) {
                            onResult(Uri.fromFile(file))
                        } else {
                            onResult(null)
                        }
                    }
                }
            }
    }

    override fun stopVideoCapture() {
        recording?.stop()
        recording = null
    }

    private fun createFileName(
        extension: String
    ): String {
        val timestamp =
            SimpleDateFormat(
                "yyyyMMdd_HHmmss_SSS",
                Locale.US
            ).format(System.currentTimeMillis())

        return "FriendZone_$timestamp.$extension"
    }
}
