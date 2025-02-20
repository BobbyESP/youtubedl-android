# youtubedl-android

Android library wrapper for [yt-dlp](https://github.com/yt-dlp/yt-dlp) (formerly [youtube-dl](https://github.com/rg3/youtube-dl)) executable.

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.junkfood02.youtubedl-android/library)

## Credits
* [youtubedl-java](https://github.com/sapher/youtubedl-java) by [sapher](https://github.com/sapher). youtubedl-android extends youtubedl-java by adding Android compatibility.

## Sample App
A debug APK for testing can be downloaded from the [releases page](https://github.com/yausername/youtubedl-android/releases).

![Download Example](https://media.giphy.com/media/fvI9yytF4rxmH7pGHu/giphy.gif)
![Streaming Example](https://media.giphy.com/media/UoqecxgY9IWbUs5tSR/giphy.gif)

### Using a Configuration File
If you wish to use a configuration file with the `--config-location` option, create a file named `config.txt` inside the `youtubedl-android` directory and add your preferred commands, for example:

```
--no-mtime
-o /sdcard/Download/youtubedl-android/%(title)s.%(ext)s
-f "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best"
```

### Related Projects
Check out [dvd](https://github.com/yausername/dvd), a video downloader app based on this library.

Also, take a look at [Seal](https://github.com/JunkFood02/Seal), another video/audio downloader app showcasing a more advanced and customized use of this library.

## Installation

### Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.junkfood02.youtubedl-android:library:0.16.0")
    implementation("io.github.junkfood02.youtubedl-android:ffmpeg:0.16.0")
    implementation("io.github.junkfood02.youtubedl-android:aria2c:0.16.0") // Optional
}
```

### Android Configuration
- Set `android:extractNativeLibs="true"` in your app's manifest.
- Use `abiFilters 'x86', 'x86_64', 'armeabi-v7a', 'arm64-v8a'` in `app/build.gradle`. See the [sample app](https://github.com/yausername/youtubedl-android/blob/master/app/build.gradle).
- Use ABI splits to reduce APK size. See the [sample app](https://github.com/yausername/youtubedl-android/blob/master/app/build.gradle).
- On Android 10 (API 29), set `android:requestLegacyExternalStorage="true"`.
- On Android 10+ (API 30 or higher), due to Scoped Storage changes, apps can only directly access the `Download/` and `Documents/` directories. See the [related issue](https://github.com/yausername/youtubedl-android/issues/174).

## Usage

### Initialization
```java
try {
    YoutubeDL.getInstance().init(this);
} catch (YoutubeDLException e) {
    Log.e(TAG, "Failed to initialize youtubedl-android", e);
}
```

### Downloading / Custom Command
A detailed example can be found in the [sample app](app/src/main/java/com/yausername/youtubedl_android_example/DownloadingExampleActivity.java).

```java
File youtubeDLDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "youtubedl-android");
YoutubeDLRequest request = new YoutubeDLRequest("https://vimeo.com/22439234");
request.addOption("-o", youtubeDLDir.getAbsolutePath() + "/%(title)s.%(ext)s");
YoutubeDL.getInstance().execute(request, (progress, etaInSeconds) -> {
    System.out.println(progress + "% (ETA " + etaInSeconds + " seconds)");
});
```

### Stopping a Download
```java
final String processId = "MyProcessDownloadId";
YoutubeDL.getInstance().execute(request, (progress, etaInSeconds) -> {
    System.out.println(progress + "% (ETA " + etaInSeconds + " seconds)");
}, processId);
...
YoutubeDL.getInstance().destroyProcessById(processId);
```

### Getting Stream Info
Equivalent to `--dump-json` in yt-dlp.

```java
VideoInfo streamInfo = YoutubeDL.getInstance().getInfo("https://vimeo.com/22439234");
System.out.println(streamInfo.getTitle());
```

### Getting a Single Playable Link (Video + Audio)
```java
YoutubeDLRequest request = new YoutubeDLRequest("https://youtu.be/Pv61yEcOqpw");
request.addOption("-f", "best");
VideoInfo streamInfo = YoutubeDL.getInstance().getInfo(request);
System.out.println(streamInfo.getUrl());
```

### Updating yt-dlp Binary
An example can be found in the [sample app](app/src/main/java/com/yausername/youtubedl_android_example/MainActivity.java).

```java
YoutubeDL.getInstance().updateYoutubeDL(this, UpdateChannel.STABLE);
```

## FFmpeg Support
To use FFmpeg features in yt-dlp (e.g., `--extract-audio`), include and initialize the FFmpeg library:

```java
try {
    YoutubeDL.getInstance().init(this);
    FFmpeg.getInstance().init(this);
} catch (YoutubeDLException e) {
    Log.e(TAG, "Failed to initialize youtubedl-android", e);
}
```

## Aria2c Support
To use `aria2c` as an external downloader, include and initialize the `aria2c` library:

```java
try {
    YoutubeDL.getInstance().init(this);
    FFmpeg.getInstance().init(this);
    Aria2c.getInstance().init(this);
} catch (YoutubeDLException e) {
    Log.e(TAG, "Failed to initialize youtubedl-android", e);
}
```

### Configuring `aria2c`
```kotlin
request.addOption("--downloader", "libaria2c.so");
```

## Documentation
- Building Python for Android: [BUILD_PYTHON.md](BUILD_PYTHON.md)
- Building FFmpeg: [BUILD_FFMPEG.md](BUILD_FFMPEG.md)
- yt-dlp Lazy Extractors: [ytdlp-lazy](https://github.com/xibr/ytdlp-lazy)
- Building `aria2`: Requires `libc++, c-ares, OpenSSL, libxml2, zlib, libiconv`. See the method used to build [Python](BUILD_PYTHON.md) or [FFmpeg](BUILD_FFMPEG.md).

## Donate
Support the project with a donation:

| Type                                                                                                   | Address                                                                                         |
|--------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| <img src="https://en.bitcoin.it/w/images/en/2/29/BC_Logo_.png" alt="Bitcoin" width="50"/>              | bc1qw3g7grh6dxk69mzwjmewanj9gj2ycc5mju5dc4                                                      |
| <img src="https://www.getmonero.org/press-kit/symbols/monero-symbol-480.png" alt="Monero" width="50"/> | 49SQgJTxoifhRB1vZGzKwUXUUNPMsrsxEacZ8bRs5tqeFgxFUHyDFBiUYh3UBRLAq355tc2694gbX9LNT7Ho7Vch2XEP4n4 |