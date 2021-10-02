import {CourseOccurrence} from './course-occurrence';

export interface Course {
  id: string;
  name: string;
  professorId: string;
  professorName: string;
  professorPhone: string;
  professorAddress: string;
  occurrences: CourseOccurrence[];
}
