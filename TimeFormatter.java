import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeFormatter {
    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");
    public static List<String> values = Arrays.asList("S", "M", "H", "D");

    public TimeFormatter() {
    }

    public static long toFormat(String value) {
        try {
            Matcher matcher = PATTERN.matcher(value);
            if (!matcher.find()) {
                return 0L;
            } else {
                int time = Integer.parseInt(matcher.group(1));
                String suffix = matcher.group(2);
                int index = values.indexOf(suffix.toUpperCase());
                int pow = (int)(index < 3 ? Math.pow(60.0D, (double)index) : Math.pow(60.0D, (double)(index - 1)) * 24.0D);
                int result = time * pow;
                return TimeUnit.SECONDS.toMillis((long)result);
            }
        } catch (Throwable var7) {
            throw var7;
        }
    }

    public static String fromFormat(long time) {
        if (time == 0L) {
            return "Nenhum";
        } else {
            long day = TimeUnit.MILLISECONDS.toDays(time);
            long hours = TimeUnit.MILLISECONDS.toHours(time) - day * 24L;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60L;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toMinutes(time) * 60L;
            StringBuilder sb = new StringBuilder();
            if (day > 0L) {
                sb.append(day).append(" ").append(day == 1L ? "dia" : "dias").append(" ");
            }

            if (hours > 0L) {
                sb.append(hours).append(" ").append(hours == 1L ? "hora" : "horas").append(" ");
            }

            if (minutes > 0L) {
                sb.append(minutes).append(" ").append(minutes == 1L ? "minuto" : "minutos").append(" ");
            }

            if (seconds > 0L) {
                sb.append(seconds).append(" ").append(seconds == 1L ? "segundo" : "segundos");
            }

            String diff = sb.toString();
            return diff.isEmpty() ? "agora" : diff;
        }
    }
}
