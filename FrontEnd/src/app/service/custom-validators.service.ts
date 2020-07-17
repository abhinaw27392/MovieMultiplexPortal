import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class CustomValidatorsService {

  constructor() { }

  // custom validators
  // for space validation
  spaceValidator(formControl: FormControl) {
    if (formControl.value != null && formControl.value.trim() == "") {
      return {
        "space": true
      }
    }
  }

  // validator for maximum number of screen
  maxNumberOfScreen(formControl: FormControl) {
    if (formControl.value != null && formControl.value > 10) {
      return {
        "maxNumber": true
      }
    }
  }
  // validator for zero/negative number not allowed in screen number feild
  zeroAndNegativeNotAllowed(formControl: FormControl) {
    if (formControl.value != null && formControl.value <= 0) {
      return {
        "zeroAndNegativeNotAllowed": true
      }
    }
  }
  dateValidator(formControl: FormControl) {
    let dateRegex = /^\d{4}[./-]\d{2}[./-]\d{2}$/;
    if (formControl.value != null) {
      if (!formControl.value.match(dateRegex)) {
        return {
          "dateValidator": true
        }
      }
    }
  }
}
