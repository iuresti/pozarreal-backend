import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CourseAssistant} from '../../model/course-assistant';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DateFormatterService} from '../../services/date-formatter.service';

@Component({
  selector: 'app-student-add',
  templateUrl: './student-add.component.html',
  styleUrls: ['./student-add.component.css']
})
export class StudentAddComponent implements OnInit {

  modelBirthDate: NgbDateStruct;
  @Input() studentData: CourseAssistant;
  @Output() dataReady: EventEmitter<void> = new EventEmitter<void>();
  @Output() wrongData: EventEmitter<void> = new EventEmitter<void>();
  minDate: NgbDateStruct;
  maxDate: NgbDateStruct;
  needsResponsible = false;

  constructor() {
  }

  ngOnInit(): void {

    const now = new Date();

    this.minDate = {
      year: now.getFullYear() - 90,
      month: 1,
      day: 1
    };

    this.maxDate = {
      year: now.getFullYear() - 1,
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    if (this.studentData.birthDate) {
      this.prepareStartDateFromExistentDate();
    } else {
      this.prepareBirthDate();
    }
  }

  prepareStartDateFromExistentDate(): void {
    this.modelBirthDate = {
      year: parseInt(this.studentData.birthDate.substr(0, 4), 10),
      month: parseInt(this.studentData.birthDate.substr(5, 2), 10),
      day: parseInt(this.studentData.birthDate.substr(8, 2), 10),
    };
    this.updateDate();
  }

  prepareBirthDate(): void {
    const now = new Date();

    this.modelBirthDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };
    this.studentData.birthDate = null;
  }

  updateDate(): void {
    this.studentData.birthDate = DateFormatterService.formatDate(this.modelBirthDate);
    this.formChanged();
  }

  formChanged(): void {
    const validPayment = this.isNameValid() &&
      this.isBirthDateValid();

    if (validPayment) {
      this.dataReady.emit();
    } else {
      this.wrongData.emit();
    }
  }

  isNameValid(): boolean {
    return !!this.studentData.name;
  }

  isBirthDateValid(): boolean {
    return !this.needsResponsible || this.modelBirthDate.year < new Date().getFullYear();
  }

  needsResponsibleChanged(): void {
    if (this.needsResponsible) {
      this.studentData.birthDate = DateFormatterService.formatDate(this.modelBirthDate);
    } else {
      this.studentData.birthDate = null;
    }
    this.formChanged();
  }
}
