import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'verification'
})
export class VerificationPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    return {
      true: 'Validado',
      false: 'Validación pendiente',
    }[value];
  }
}
