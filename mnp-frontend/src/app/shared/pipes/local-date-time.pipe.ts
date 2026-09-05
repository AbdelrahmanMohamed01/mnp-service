import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'localDateTime',
  standalone: true,
})
export class LocalDateTimePipe implements PipeTransform {
  private readonly timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

  transform(value: string | Date | null | undefined): string {
    if (!value) {
      return '';
    }

    const date = value instanceof Date ? value : new Date(value);
    if (isNaN(date.getTime())) {
      return '';
    }

    return new Intl.DateTimeFormat(undefined, {
      timeZone: this.timeZone,
      year: 'numeric',
      month: 'short',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: true,
    }).format(date);
  }
}
