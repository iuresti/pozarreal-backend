import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'subConcept'
})
export class SubConceptPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): unknown {
    return {
      MAINTENANCE_BIM_1: 'Bimestre 1',
      MAINTENANCE_BIM_2: 'Bimestre 2',
      MAINTENANCE_BIM_3: 'Bimestre 3',
      MAINTENANCE_BIM_4: 'Bimestre 4',
      MAINTENANCE_BIM_5: 'Bimestre 5',
      MAINTENANCE_BIM_6: 'Bimestre 6',
    }[value];
  }

}
