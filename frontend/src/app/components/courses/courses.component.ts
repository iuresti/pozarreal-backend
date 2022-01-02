import {Component, OnInit} from '@angular/core';
import {Course} from '../../model/course';
import {CourseAssistant} from '../../model/course-assistant';
import {CourseService} from '../../services/course.service';
import {CourseAssistantService} from '../../services/course-assistant.service';
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import {CourseAssistantPayment} from '../../model/course-assistant-payment';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {
  loading = false;
  students: CourseAssistant[] = [];
  courses: Course[] = [];
  selectedCourseId: string = null;
  selectedCourse: Course;
  newStudentReady = false;
  newStudent: CourseAssistant;
  studentForPayments: CourseAssistant;
  showPaymentsForStudents: string;
  paymentData: CourseAssistantPayment;

  constructor(private courseService: CourseService,
              private courseAssistantService: CourseAssistantService,
              private modalService: NgbModal,
              config: NgbModalConfig) {
    config.backdrop = 'static';
    config.keyboard = false;
  }

  ngOnInit(): void {
    this.loading = true;
    this.courseService.getAllCourses().subscribe(courses => {
      this.courses = courses;
      this.loading = false;
    });
  }

  loadStudents(): void {
    console.log(this.selectedCourseId);
    if (this.selectedCourseId) {
      this.loading = true;
      this.selectedCourse = this.courses.filter(course => course.id === this.selectedCourseId)[0];
      this.courseAssistantService.getCourseAssistants(this.selectedCourseId).subscribe(students => {
        this.students = students;
        this.loading = false;
      });
    } else {
      this.students = [];
    }
  }

  openStudentDialog(content): void {
    this.newStudent = {} as CourseAssistant;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {
      console.log('Saving student');
      console.log(this.newStudent);
      this.newStudent.courseId = this.selectedCourseId;
      this.courseAssistantService.save(this.newStudent).subscribe(savedStudent => {
        console.log(savedStudent);
        this.loadStudents();
      });

    }, () => {
      console.log('Cancel saving student');
    });

  }

  showPayments(content, student: CourseAssistant): void {
    if (this.showPaymentsForStudents === student.id) {
      this.studentForPayments = null;
      this.showPaymentsForStudents = null;
    } else {
      this.studentForPayments = student;
      this.showPaymentsForStudents = student.id;
    }
  }

  deleteStudent(student: CourseAssistant): void {
    Swal.fire({
      title: `¿Confirmas la eliminación del estudiante ${student.name} ?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.courseAssistantService.delete(student.id).subscribe(() => {
          this.loadStudents();
          Swal.fire('Eliminado!', '', 'success').then(() => {});
        });
      }
    });
  }

  editStudent(addStudentDialog, student: CourseAssistant): void {
    this.newStudent = {} as CourseAssistant;

    this.newStudent.id = student.id;
    this.newStudent.name = student.name;
    this.newStudent.courseId = this.selectedCourseId;
    this.newStudent.birthDate = student.birthDate;
    this.newStudent.phone = student.phone;
    this.newStudent.responsibleName = student.responsibleName;
    this.newStudent.responsiblePhone = student.responsiblePhone;
    this.newStudent.responsibleName2 = student.responsibleName2;
    this.newStudent.responsiblePhone2 = student.responsiblePhone2;
    this.newStudent.email = student.email;
    this.newStudent.notes = student.notes;

    this.modalService.open(addStudentDialog, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      console.log('Saving student');
      console.log(this.newStudent);
      this.courseAssistantService.save(this.newStudent).subscribe(() => {
        this.loadStudents();
      });
    }, (reason) => {
      console.log('Cancel saving student');
    });
  }

  registerPayments(content, student: CourseAssistant): void {
    this.paymentData = {} as CourseAssistantPayment;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title', size: 'xl'}).result.then((result) => {
      console.log('Payments closed');
    });
  }
}
