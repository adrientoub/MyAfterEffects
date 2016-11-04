# MyAfterEffects

## Description

This project is a complete video editing software. The program allows for
opening images and videos, and moving them in the a timeline. This allows you
to tell your story and create a video montage. Then you can apply filters to
each of the medias individually. The list of current filters is down bellow.

You can split any of your Media (Video, Image or Sequence) into a part of it.
This is especially useful to keep only a part of a media, or applying a filter
only to a part of a video for instance.

When you have finished editing your video, you can export it using the
*File->Export* button in the menu. It allows you to export following the
settings you set in the program main page. By default it only exports the first
*100 frames* but this is configurable.

This project is optimized for performance by being multithreaded. When rendering
you can choose how many threads will be created to split the processing. The
default value is *2 times the processor count* in the current system. There are
several [benchmarks](BENCHMARK.md) available to display the gain of this
parallelization (your gains might be different depending on your Operating
System, Medias, Storage and Processor).

### Filters

Those filters are currently implemented:
* Binarize
* Border Detection
* Cartoonify
* Chroma Key
* Grayscale
* Low Pass
* Otsu Binarize
* Quantize Colors
* Sepia
* Sharpen
* Transparency

## Limitations

This project only works on the images contained in the videos. There is no audio
at all. There is no possibility to use this project for a real movie creation
because of this, but you can use it in conjunction with another software
allowing you to mix music for maximum usability.

## Compatibility

Supported platforms are:

* Windows x64 (tested on Windows 10)
* Linux x64

### Windows x64

All DLL files (shared libraries) needed are already provided with this repository.

Requirements are:

* Gradle 2.12 or later (you can install it using Chocolatey)
* Java 8

### Linux x64

Requirements are:

* Java 8
* Gradle 2.12 or later
* OpenCV 3.1.0

## Usage

To launch MyAfterEffects, run at the root of this repository:
```
gradle run
```
