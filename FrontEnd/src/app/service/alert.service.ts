import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { Router, NavigationStart } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private router: Router) {
    // clear alert message on route change
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.subject.next();
      }
    });
  }
  // communication between service and component using custom subject and custom Observable
  private subject = new Subject<any>();

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

  success(message: string, isSuccess: boolean) {
    this.subject.next({ message: message, isSuccess: isSuccess });
  }

  error(message: string, isError: boolean) {
    this.subject.next({ message: message, isError: isError });
  }

  info(message: string, isInfo: boolean) {
    this.subject.next({ message: message, isInfo: isInfo });
  }

  warn(message: string, isWaning: boolean) {
    this.subject.next({ message: message, isWaning: isWaning });
  }

}
