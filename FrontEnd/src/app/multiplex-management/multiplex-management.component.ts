import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { trim } from 'jquery';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';

import { Multiplex } from './../model/multiplex';
import { MultiplexDataService } from './../service/data/multiplex-data.service';


@Component({
  selector: 'app-multiplex-management',
  templateUrl: './multiplex-management.component.html',
  styleUrls: ['./multiplex-management.component.css']
})
export class MultiplexManagementComponent implements OnInit, AfterViewInit, OnDestroy {
  multiplexData: Multiplex[];
  multiplex: any = "";
  modalTitle: string;
  // hardcoded for now 
  userId = "5efefe20f1975c11122e8ca7";

  multiplexName: string;
  address: string;
  numberOfScreens: number;

  multiplexFormGroup: FormGroup;
  constructor(private multiplexService: MultiplexDataService, private formBuilder: FormBuilder) {
    this.multiplexFormGroup = formBuilder.group({
      "multiplex_name": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(30), this.spaceValidator])),
      "address": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(40), this.spaceValidator])),
      "number_of_screen": new FormControl("", Validators.compose([Validators.required, this.zeroAndNegativeNotAllowed, this.maxNumberOfScreen]))
    });
  }
  // custom validators
  // for space validation
  spaceValidator(formControl: FormControl) {
    if (formControl.value != "" && trim(formControl.value) == "") {
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
  // validator for zero not allowed
  zeroAndNegativeNotAllowed(formControl: FormControl) {
    if (formControl.value != null && formControl.value <= 0) {
      return {
        "zeroAndNegativeNotAllowed": true
      }
    }
  }

  getAllMultiplexData() {
    this.multiplexService.getallData().subscribe((data: Multiplex[]) => {
      this.multiplexData = data;
      this.rerender();
    }, (err) => { console.log(err) });
  }

  saveMultiplex() {
    if (this.multiplex === "") {
      this.addMultiplex();
    } else {
      this.updateMultiplex();
    }
  }

  addMultiplex() {
    this.getValueFromForm();
    this.multiplexService.addMultiplex(new Multiplex(this.multiplexName, this.address, this.numberOfScreens), this.userId).subscribe(
      (response: Multiplex) => {
        console.log(response);
        this.multiplexData.push(response);
        this.getAllMultiplexData();
        this.rerender();
        this.multiplexFormGroup.reset();
      }, (err) => {
        console.log(err);
      }
    );
  }

  getEachRowMultiplexDetails(id: string) {
    this.modalTitle = "EDIT";
    this.multiplexService.getOneMultiplex(id).subscribe(
      (data) => {
        this.multiplex = data;
        console.log(this.multiplex);
        this.setValueToEditForm();
      }, (err) => {
        console.log(err);
      }
    );
  }

  setValueToEditForm() {
    this.multiplexFormGroup.controls['multiplex_name'].setValue(this.multiplex.multiplexName);
    this.multiplexFormGroup.controls['address'].setValue(this.multiplex.address);
    this.multiplexFormGroup.controls['number_of_screen'].setValue(this.multiplex.numberOfScreens);
  }

  getValueFromForm() {
    this.multiplexName = this.multiplexFormGroup.controls['multiplex_name'].value;
    this.address = this.multiplexFormGroup.controls['address'].value;
    this.numberOfScreens = this.multiplexFormGroup.controls['number_of_screen'].value;
  }

  onClickOnAdd() {
    this.modalTitle = "ADD";
    this.multiplexFormGroup.reset();
  }

  updateMultiplex() {
    this.getValueFromForm();
    console.log(this.multiplexName);
    this.multiplexService.updateMultiplex(this.multiplex.id, new Multiplex(this.multiplexName, this.address, this.numberOfScreens),
      this.userId).subscribe(
        (response: Multiplex) => {
          console.log(response);
          this.getAllMultiplexData();
          this.rerender();
          this.multiplexFormGroup.reset();
          this.multiplex = "";
        }, (err) => {
          console.log(err);
        }
      );
  }

  onClickOnDelete() {
    console.log(this.multiplex.id);
    this.multiplexService.deleteMultiplex(this.multiplex.id, this.userId).subscribe(
      (response) => {
        console.log(response);
        this.getAllMultiplexData();
        this.rerender();
        this.multiplexFormGroup.reset();
        this.multiplex = "";
      }
    )
  }

  // data table implementation with rerendering of data
  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject();

  ngOnInit(): void {
    this.getAllMultiplexData();
    this.dtOptions = {
      pagingType: 'full_numbers',
      order: [1, 'asc'],
      pageLength: 9,
      processing: true,
      deferLoading: 4,
      responsive: true
    };
  }

  rerender(): void {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      // Destroy the table first
      dtInstance.destroy();
      // Call the dtTrigger to rerender again
      this.dtTrigger.next();
    });
  }

  ngAfterViewInit(): void {
    this.dtTrigger.next();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

}
