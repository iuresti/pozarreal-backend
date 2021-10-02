import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'paymentMode'
})
export class PaymentModePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): string {
    if (value === 'CASH') {
      return 'Efectivo';
    } else {
      return 'Transferencia';
    }
  }

}
