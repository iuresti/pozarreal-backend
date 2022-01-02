import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Notification} from '../model/notification';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) {
  }

  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${environment.baseUrl}/notifications`);
  }

  saveNotification(message: string): Observable<Notification> {
    return this.http.post<Notification>(`${environment.baseUrl}/notifications`, message);
  }

  readNotification(notificationId: string): Observable<Notification> {
    return this.http.patch<Notification>(`${environment.baseUrl}/notifications/${notificationId}`, {});
  }

  deleteNotification(notificationId: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/notifications/${notificationId}`);
  }
}
