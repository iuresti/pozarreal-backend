import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

export class DateFormatterService {

  constructor() {
  }

  static formatDate(date: NgbDateStruct): string {
    return `${DateFormatterService.pad(date.year)}-${DateFormatterService.pad(date.month)}-${DateFormatterService.pad(date.day)}`;
  }

  static pad(value: number): string {
    return value < 10 ? '0' + value : '' + value;
  }
}
