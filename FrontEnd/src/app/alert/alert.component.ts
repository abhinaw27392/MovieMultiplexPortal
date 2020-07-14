import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlertService } from './../service/alert.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit, OnDestroy {
  isSuccess: boolean;
  isError: boolean;
  isWarning: boolean;
  isInfo: boolean;
  message: string;

  private subscription: Subscription;
  constructor(public alertService: AlertService) {
  }

  myAlertFunction(res) {
    if (res != undefined) {
      console.log("isSuccess" + res.isSuccess);
      this.message = res.message;
      this.isSuccess = res.isSuccess;
      this.isError = res.isError;
      this.isInfo = res.isInfo;
      this.isWarning = res.isWarning;
    }
    else {
      this.message = "";
      this.isSuccess = false;
      this.isError = false;
      this.isInfo = false;
      this.isWarning = false;
    }
  }

  ngOnInit(): void {
    this.subscription = this.alertService.getMessage().subscribe(res => {
      this.myAlertFunction(res);
    });
  }

  closeSuccess() {
    this.isSuccess = false;
  }

  closeError() {
    this.isError = false;
  }

  closeInfo() {
    this.isInfo = false;
  }

  closeWarning() {
    this.isWarning = false;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
