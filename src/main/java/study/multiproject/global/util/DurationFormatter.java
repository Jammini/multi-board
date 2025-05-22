package study.multiproject.global.util;

import java.time.Duration;

public class DurationFormatter {

    public static String formatDurationInKorean(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("일 ");
        }
        if (hours > 0) {
            sb.append(hours).append("시간 ");
        }
        if (minutes > 0 || sb.isEmpty()) {
            sb.append(minutes).append("분 ");
        }
        return sb.toString().trim();
    }
}
