import { Component, OnInit } from '@angular/core';

import { AuthenticationService } from './../service/authentication.service';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Login } from '../model/login';
import { CustomValidatorsService } from '../service/custom-validators.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  credentials = { username: '', password: '' };
  loginFormLables: string[] = ['Username', 'Password'];
  loginFormGroup: FormGroup;

  constructor(private authService: AuthenticationService, private formBuilder: FormBuilder,
    private customValidators: CustomValidatorsService) {
    this.loginFormGroup = this.formBuilder.group({
      "username": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(10), this.customValidators.spaceValidator])),
      "password": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(20), this.customValidators.spaceValidator]))
    })
  }

  getValueFromForm() {
    this.credentials.username = this.loginFormGroup.controls['username'].value;
    this.credentials.password = this.loginFormGroup.controls['password'].value;
  }
  login() {
    this.getValueFromForm();
    this.authService.authenticate(new Login(this.credentials.username, this.credentials.password));
  }

  ngOnInit(): void {
  }
}
