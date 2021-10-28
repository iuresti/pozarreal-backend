import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'verification'
})
export class VerificationPipe implements PipeTransform {

  transform(value: boolean, ...args: unknown[]): string {
    return value ? 'Validado' : 'Validación pendiente';
  }

}
