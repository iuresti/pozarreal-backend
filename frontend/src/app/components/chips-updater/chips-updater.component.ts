import {Component, OnInit} from '@angular/core';
import {UploadFileService} from '../../services/upload-file.service';
import {Observable} from 'rxjs';
import {HttpEventType, HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-chips-updater',
  templateUrl: './chips-updater.component.html',
  styleUrls: ['./chips-updater.component.css']
})
export class ChipsUpdaterComponent implements OnInit {

  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';
  urlFile: string;

  fileInfos: Observable<any>;

  constructor(private uploadService: UploadFileService) {
  }

  ngOnInit(): void {
  }

  selectFile(event): void {
    this.selectedFiles = event.target.files;
    this.message = '';
    this.urlFile = null;
  }

  upload(): void {
    this.progress = 0;

    this.currentFile = this.selectedFiles.item(0);
    this.uploadService.upload(this.currentFile).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.urlFile = event.body.fileInfo.url;
        }
      },
      () => {
        this.progress = 0;
        this.message = 'No fue posible actualizar la base de datos!';
        this.currentFile = undefined;
      });

    this.selectedFiles = undefined;
  }
}
