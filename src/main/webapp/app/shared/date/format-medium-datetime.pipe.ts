import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDatetime',
})
export class FormatMediumDatetimePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): any {
    return day ? day.format('YYYY/MM/D') : '';
  }
}
