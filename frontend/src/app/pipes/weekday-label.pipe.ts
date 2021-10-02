import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'weekdayLabel'
})
export class WeekdayLabelPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    return {
      MONDAY: 'Lunes',
      TUESDAY: 'Martes',
      WEDNESDAY: 'Miércoles',
      THURSDAY: 'Jueves',
      FRIDAY: 'Viernes',
      SATURDAY: 'Sábado',
      SUNDAY: 'Domingo'
    }[value];
  }

}
