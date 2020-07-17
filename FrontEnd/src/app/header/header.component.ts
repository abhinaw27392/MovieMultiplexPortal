import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../service/alert.service';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthenticationService) { }

  logOut() {
    this.authService.logOut();
  }

  ngOnInit(): void {
  }

}
