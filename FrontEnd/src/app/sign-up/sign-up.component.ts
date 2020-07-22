import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { AlertService } from '../service/alert.service';
import { AuthenticationService } from '../service/authentication.service';
import { CustomValidatorsService } from '../service/custom-validators.service';
import { RegisterService } from './../service/register.service';
import { SignUp } from './../model/signup';
import { UserDetail } from '../model/userDetail';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  userName: string;
  password: string;
  confirmPassword: string;
  emailId: string;
  firstName: string;
  lastName: string;
  role: string;
  signUpFormLables: string[] = ['User Name', 'Password', 'Confirm Password', 'Email Id', 'First name', 'Last name', 'Role'];
  signupFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private alertService: AlertService, private authService: AuthenticationService,
    private customValidators: CustomValidatorsService, private registerService: RegisterService) {
    this.signupFormGroup = formBuilder.group({
      "userName": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(10), this.customValidators.spaceValidator])),
      "password": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(20), this.customValidators.spaceValidator])),
      "confirmPassword": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(20), this.customValidators.spaceValidator])),
      "emailId": new FormControl("", Validators.compose([Validators.required, Validators.email])),
      "firstName": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(20), this.customValidators.spaceValidator])),
      "lastName": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(20), this.customValidators.spaceValidator])),
      "role": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(10), this.customValidators.spaceValidator]))
    });
  }

  getValueFromForm() {
    this.userName = this.signupFormGroup.controls['userName'].value;
    this.password = this.signupFormGroup.controls['password'].value;
    this.confirmPassword = this.signupFormGroup.controls['confirmPassword'].value;
    this.emailId = this.signupFormGroup.controls['emailId'].value;
    this.firstName = this.signupFormGroup.controls['firstName'].value;
    this.lastName = this.signupFormGroup.controls['lastName'].value;
    this.role = this.signupFormGroup.controls['role'].value;
  }

  saveUser() {
    this.getValueFromForm();
    this.registerService.register(new SignUp(this.userName, this.password, this.confirmPassword, this.emailId,
      this.firstName, this.lastName, this.role))
      .subscribe((res: UserDetail) => {
        this.alertService.success("User Registered successfully!", true);
        this.signupFormGroup.reset();
      }, err => {
        this.alertService.error(err.error.message, true);
      });
  }

  ngOnInit(): void {
  }

}
