import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'role'
})
export class RolePipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    return {
      ROLE_ADMIN: 'Administrador de Pozarreal',
      ROLE_RESIDENT: 'Residente',
      ROLE_REPRESENTATIVE: 'Representante',
      ROLE_USER_MANAGER: 'Administrador de usuarios',
      ROLE_CHIPS_UPDATER: 'Actualizador de Chips',
      ROLE_SCHOOL_MANAGER: 'Administrador de cursos'
    }[value];
  }

}
