import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';

import { MovieDataService } from './../service/data/movie-data.service';
import { Movie } from './../model/movie'
import { AlertService } from './../service/alert.service';

@Component({
  selector: 'app-movie-management',
  templateUrl: './movie-management.component.html',
  styleUrls: ['./movie-management.component.css']
})
export class MovieManagementComponent implements OnInit, AfterViewInit, OnDestroy {
  movieData: Movie[];
  movie: any = "";
  modalTitle: string;
  // hardcoded for now 
  userId = "5efefe20f1975c11122e8ca7";

  movieName: string;
  movieCategory: string;
  movieProducer: string;
  movieDirector: string;
  releaseDate: string;

  movieFormGroup: FormGroup;
  constructor(private movieService: MovieDataService, private formBuilder: FormBuilder, private alertService: AlertService) {
    this.movieFormGroup = formBuilder.group({
      "movie_name": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(30), this.spaceValidator])),
      "category": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(30), this.spaceValidator])),
      "producer": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(30), this.spaceValidator])),
      "director": new FormControl("", Validators.compose([Validators.required, Validators.maxLength(30), this.spaceValidator])),
      "release_date": new FormControl("", Validators.compose([Validators.required, this.dateValidator]))
    });
  }
  // custom validators
  // for space validation
  spaceValidator(formControl: FormControl) {
    if (formControl.value != null && formControl.value.trim() == "") {
      return {
        "space": true
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

  getAllMovieData() {
    console.log("getAllMovieData executing.........");
    this.movieService.getallData().subscribe((data: Movie[]) => {
      this.movieData = data;
      this.rerender();
    }, (err) => { console.log(err) });
  }

  saveMovie() {
    if (this.movie === "") {
      this.addMovie();
    } else {
      this.updateMovie();
    }
  }

  addMovie() {
    this.getValueFromForm();
    this.movieService.addMovie(new Movie(this.movieName, this.movieCategory, this.movieProducer, this.movieDirector, this.releaseDate), this.userId).subscribe(
      (response: Movie) => {
        console.log(response);
        this.movieData.push(response);
        this.alertService.success("Movie added successfully!", true);
        this.getAllMovieData();
        this.rerender();
        this.movieFormGroup.reset();
      }, (err) => {
        this.alertService.error(err, true);
      }
    );
  }

  getEachRowMovieDetails(id: string) {
    this.modalTitle = "EDIT";
    this.movieService.getOneMovie(id).subscribe(
      (data) => {
        this.movie = data;
        this.setValueToEditForm();
      }, (err) => {
        this.alertService.error(err, true);
      }
    );
  }

  setValueToEditForm() {
    this.movieFormGroup.controls['movie_name'].setValue(this.movie.movieName);
    this.movieFormGroup.controls['category'].setValue(this.movie.movieCategory);
    this.movieFormGroup.controls['producer'].setValue(this.movie.movieProducer);
    this.movieFormGroup.controls['director'].setValue(this.movie.movieDirector);
    this.movieFormGroup.controls['release_date'].setValue(this.movie.releaseDate);
  }

  getValueFromForm() {
    this.movieName = this.movieFormGroup.controls['movie_name'].value;
    this.movieCategory = this.movieFormGroup.controls['category'].value;
    this.movieProducer = this.movieFormGroup.controls['producer'].value;
    this.movieDirector = this.movieFormGroup.controls['director'].value;
    this.releaseDate = this.movieFormGroup.controls['release_date'].value;
  }

  onClickOnAdd() {
    this.modalTitle = "ADD";
    this.movieFormGroup.reset();
  }

  updateMovie() {
    this.getValueFromForm();
    this.movieService.updateMovie(this.movie.id, new Movie(this.movieName, this.movieCategory, this.movieProducer, this.movieDirector, this.releaseDate), this.userId).subscribe(
      (response: Movie) => {
        console.log(response);
        this.alertService.success("Movie updated successfully!", true);
        this.getAllMovieData();
        this.rerender();
        this.movieFormGroup.reset();
        this.movie = "";
      }, (err) => {
        this.alertService.error(err, true);
      }
    );
  }

  onClickOnDelete() {
    console.log(this.movie.id);
    this.movieService.deleteMovie(this.movie.id, this.userId).subscribe(
      (response) => {
        console.log(response);
        this.alertService.success("Movie deleted successfully!", true);
        this.getAllMovieData();
        this.rerender();
        this.movieFormGroup.reset();
        this.movie = "";
      }, err => {
        this.alertService.error(err, true);
      }
    )
  }

  // data table implementation with rerendering of data
  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject();

  ngOnInit(): void {
    this.getAllMovieData();
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
