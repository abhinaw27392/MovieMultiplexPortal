import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';

import { MovieMultiplexDetails } from '../model/movie-multiplex-details';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { CustomValidatorsService } from '../service/custom-validators.service';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { MovieDataService } from '../service/data/movie-data.service';
import { MultiplexDataService } from '../service/data/multiplex-data.service';
import { AlertService } from '../service/alert.service';
import { Movie } from '../model/movie';
import { Multiplex } from '../model/multiplex';
import { AllotMovieService } from './../service/data/allot-movie.service';
import { MovieMultiplex } from '../model/movie-multiplex';

@Component({
  selector: 'app-allot-movie',
  templateUrl: './allot-movie.component.html',
  styleUrls: ['./allot-movie.component.css']
})
export class AllotMovieComponent implements OnInit, AfterViewInit, OnDestroy {

  modalTitle: string;
  userId = this.authService.userId;
  movieData = [];
  multiplexData = [];
  screenData = [];
  screenDataNew: any;
  movieMultiplexDetailsData: MovieMultiplexDetails[];
  movieMultiplex: any = "";

  movieId: any;
  multiplexId: string;
  screenName: string;

  screenCheck: boolean;
  screenDropdownSettings: IDropdownSettings = {};
  movieDropdownSettings: IDropdownSettings = {};
  multiplexDropdownSettings: IDropdownSettings = {};

  movieAllottedFormLabels: string[] = ['Movie Name', 'Multiplex Name', 'Screen Number'];
  movieMultiplexFormGroup: FormGroup;

  constructor(private authService: AuthenticationService, private formBuilder: FormBuilder, private movieService: MovieDataService,
    private multiplexService: MultiplexDataService, private alertService: AlertService,
    private allotMovieService: AllotMovieService) {
    this.movieMultiplexFormGroup = this.formBuilder.group({
      "movie_name": new FormControl("", Validators.compose([Validators.required])),
      "multiplex_name": new FormControl("", Validators.compose([Validators.required])),
      "screen_name": new FormControl("", Validators.compose([Validators.required]))
    })
  }

  getMovieMultiplexData() {
    this.allotMovieService.getallData().subscribe((data: MovieMultiplexDetails[]) => {
      this.movieMultiplexDetailsData = data;
      console.log(this.movieMultiplexDetailsData);
      this.rerender();
    }, (err) => {
      this.alertService.error(err.error.message, true);
    });
  }

  saveMovieMultiplex() {
    // this.getValueFromForm();
    if (this.movieMultiplex === "") {
      this.addMovieMultiplex();
    } else {
      this.updateMovieMultiplex();
    }
  }

  getValueFromForm() {
    this.movieId = this.movieMultiplexFormGroup.controls['movie_name'].value[0].id;
    this.multiplexId = this.movieMultiplexFormGroup.controls['multiplex_name'].value[0].id;
    this.screenName = this.movieMultiplexFormGroup.controls['screen_name'].value[0].screenName;
  }

  addMovieMultiplex() {
    this.getValueFromForm();
    console.log(this.movieId);
    console.log(this.multiplexId);
    console.log(this.screenName);
    this.allotMovieService.addMovieMultiplex(this.userId, new MovieMultiplex(this.movieId, this.multiplexId, this.screenName)).subscribe(
      (response: MovieMultiplexDetails) => {
        this.movieMultiplexDetailsData.push(response);
        this.alertService.success("Movie allotment added successfully!", true);
        this.getMovieMultiplexData();
        this.rerender();
        this.movieMultiplexFormGroup.reset();
      }, (err) => {
        this.alertService.error(err.error.message, true);
      }
    );
  }

  getEachRowAllottedMovieDetails(id: string) {
    this.getAllData();
    this.modalTitle = "EDIT";
    this.allotMovieService.getOneMovieMultiplex(id).subscribe(
      (data) => {
        this.movieMultiplex = data;
        console.log(this.movieMultiplex);
        this.setValueToEditForm();
      }, (err) => {
        this.alertService.error(err.error.message, true);
      }
    );
  }

  setValueToEditForm() {
    this.movieMultiplexFormGroup.controls['movie_name'].setValue([{
      id: this.movieMultiplex.movie.id, movieName: this.movieMultiplex.movie.movieName
    }]);
    this.movieMultiplexFormGroup.controls['multiplex_name'].setValue([{
      id: this.movieMultiplex.multiplex.id, multiplexName: this.movieMultiplex.multiplex.multiplexName
    }]);
    this.movieMultiplexFormGroup.controls['screen_name'].setValue([{
      id: this.movieMultiplex.id, screenName: this.movieMultiplex.screenName
    }]);
  }

  updateMovieMultiplex() {
    this.getValueFromForm();
    console.log(this.movieMultiplex.id);
    console.log(this.movieId);
    console.log(this.multiplexId);
    console.log(this.screenName);
    this.allotMovieService.updateMovieMultiplex(this.movieMultiplex.id, new MovieMultiplex(this.movieId, this.multiplexId, this.screenName),
      this.userId).subscribe(
        (response: Movie) => {
          this.alertService.success("Movie allotment updated successfully!", true);
          this.getMovieMultiplexData();
          this.rerender();
          this.movieMultiplexFormGroup.reset();
          this.movieMultiplex = "";
        }, (err) => {
          this.alertService.error(err.error.message, true);
        }
      );
  }

  getAllData() {
    this.movieService.getallData().subscribe((data: Movie[]) => {
      this.movieData = data;
      console.log(this.movieData);
      this.rerender();
    }, (err) => {
      this.alertService.error(err.error.message, true);
    });
    this.multiplexService.getallData().subscribe((data: Multiplex[]) => {
      this.multiplexData = data;
    }, err => {
      this.alertService.error(err.error.message, true);
    });
  }

  onClickOnAdd() {
    this.getAllData();
    this.modalTitle = "ADD";
    this.movieMultiplexFormGroup.reset();
  }

  onClickOnDelete() {
    this.allotMovieService.deleteMovieMultiplex(this.movieMultiplex.id, this.userId).subscribe(
      (response) => {
        this.alertService.success("Movie allotment deleted successfully!", true);
        this.getMovieMultiplexData();
        this.rerender();
        this.movieMultiplexFormGroup.reset();
        this.movieMultiplex = "";
      }, err => {
        this.alertService.error(err.error.message, true);
      }
    )
  }

  onMultiplexSelect(item: any) {
    this.screenData = [];
    this.screenCheck = false;
    this.multiplexService.getOneMultiplex(item.id).subscribe((data: Multiplex) => {
      for (let i = 1; i <= data.numberOfScreens; i++) {
        this.screenData.push({ id: i, screenName: "SCR-" + i });
      }
      this.screenCheck = true;
      this.screenDataNew = this.screenData;
    }, err => {
      console.log(err);
    });

    this.screenDropdownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'screenName',
      itemsShowLimit: 3,
      allowSearchFilter: true,
      closeDropDownOnSelection: true,
      clearSearchFilter: true
    };
  }

  // data table implementation with rerendering of data
  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject();

  ngOnInit(): void {
    this.getMovieMultiplexData();
    this.movieDropdownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'movieName',
      allowSearchFilter: true,
      closeDropDownOnSelection: true,
      clearSearchFilter: true
    };
    this.multiplexDropdownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'multiplexName',
      allowSearchFilter: true,
      closeDropDownOnSelection: true,
      clearSearchFilter: true
    };
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
