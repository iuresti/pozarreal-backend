import {Component, Input, OnInit} from '@angular/core';
import {Course} from '../../model/course';

@Component({
  selector: 'app-course-info',
  templateUrl: './course-info.component.html',
  styleUrls: ['./course-info.component.css']
})
export class CourseInfoComponent implements OnInit {

  @Input() course: Course;

  constructor() { }

  ngOnInit(): void {
  }

}
