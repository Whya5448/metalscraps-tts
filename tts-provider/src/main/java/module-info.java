module dev.whya.tts.provider {
    requires dev.whya.tts.core;

    requires kotlin.stdlib;
    requires com.fasterxml.jackson.kotlin;
    requires java.xml.bind;

    exports dev.whya.tts.providers;
}