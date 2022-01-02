import {Component, OnInit} from '@angular/core';
import {NotificationService} from '../../services/notification.service';
import {Notification} from '../../model/notification';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];
  loading = true;
  notificationsNoRead: number;

  constructor(private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.getAllNotifications();
  }

  getAllNotifications(): void {
    this.notificationService.getNotifications().subscribe(notifications => {
      this.notifications = notifications;
      this.notificationsNoRead = notifications.filter(notification => !notification.seen).length;
      this.loading = false;
    });
  }

  getNoReadNotifications(): void {
    this.notificationService.getNotifications().subscribe(notifications => {
      this.notifications = notifications.filter(notification => !notification.seen);
      this.notificationsNoRead = this.notifications.length;
    });
  }

  readNotification(notification: Notification): void {
    notification.seen = true;
    this.notificationService.readNotification(notification.id).subscribe();
  }

  deleteNotification(notification: Notification): void {
    Swal.fire({
      title: '¿Confirmas la eliminación de esta notificación?',
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Sí',
      denyButtonText: 'No',
    }).then(result => {
      if (result.isConfirmed) {
        this.notificationService.deleteNotification(notification.id).subscribe(() => {
          Swal.fire('Eliminado!', '', 'success').then(() => {
            this.getAllNotifications();
          });
        });
      }
    });
  }

  readAllNotifications(): void {
    this.notifications.forEach(notification => {
      if (!notification.seen) {
        this.notificationService.readNotification(notification.id).subscribe(() => notification.seen = true);
      }
    });
  }

}
