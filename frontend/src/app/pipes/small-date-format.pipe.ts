import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'smallDateFormat'
})
export class SmallDateFormatPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    const year = value.substr(2, 2);
    const month = value.substr(5, 2);
    const day = value.substr(8, 2);

    return day + '-' + month + '-' + year;
  }

}
