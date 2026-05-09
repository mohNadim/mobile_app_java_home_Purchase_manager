package com.example.bmp601_hw.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * مدير الأصوات (Audio Manager)
 * يدير تشغيل المؤثرات الصوتية في التطبيق
 * يستخدم ToneGenerator لتشغيل النبرات الصوتية
 */
public class AudioHelper {
    private ToneGenerator toneGenerator;
    private Context context;

    public AudioHelper(Context context) {
        this.context = context;
        try {
            // إنشاء ToneGenerator على قناة STREAM_MUSIC
            toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSaveSound() {
        if (toneGenerator != null) {
            // تشغيل نبرة تأكيد (صوت تنبيه)
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
        }
    }

    public void playClickSound() {
        if (toneGenerator != null) {
            // تشغيل نبرة قصيرة للنقر
            toneGenerator.startTone(ToneGenerator.TONE_PROP_PROMPT, 50);
        }
    }

    public void release() {
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
    }

    public void stopAllSounds() {
        if (toneGenerator != null) {
            toneGenerator.release();
            try {
                toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeAllSounds() {
    }
}
