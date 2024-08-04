package radhouene.develop.nakivo.nakivoapp.entities;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Schedules {
      private String       name;
      private Boolean      enabled;
      private String       startTime;
      private String       endTime;
      private Integer       dayOfWeek;
      private Integer       dayOfMonth;
      private String       nextRun;
      private String      scheduleRetentionType;

}
