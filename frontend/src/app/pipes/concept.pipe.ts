import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'concept'
})
export class ConceptPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): unknown {
    return {
      MAINTENANCE: 'Mantenimiento',
      ACCESS_CHIPS: 'Tarjeta de acceso',
      PARKING_PEN: 'Pluma',
      COMMON_AREA_CONSTRUCTION: 'Construcción area común',
    }[value];
  }

}



