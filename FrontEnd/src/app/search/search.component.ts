import { Component, OnInit, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';

import { SearchResult } from './../model/searchResult';
import { FormBuilder, FormControl, Validators, FormGroup } from '@angular/forms';
import { CustomValidatorsService } from '../service/custom-validators.service';
import { SearchService } from './../service/data/search.service';
import { AlertService } from '../service/alert.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchData: SearchResult[];
  searchResultFormLabels: string[] = ['Movie Name', 'Multiplex Name', 'Address', 'Screen Number'];
  isMovie: boolean = true;
  isMultiplex: boolean = false;
  isData: boolean;
  dataNotFound: boolean;
  searchFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private customValidators: CustomValidatorsService, private searchService: SearchService,
    private alertService: AlertService) {
    this.searchFormGroup = this.formBuilder.group({
      "movie_search": new FormControl("", Validators.compose([Validators.maxLength(20)])),
      "multiplex_search": new FormControl("", Validators.compose([Validators.maxLength(20)]))
    })
  }

  isMovieSelect() {
    this.isMovie = true;
    this.isMultiplex = false;
    this.searchFormGroup.reset();
    this.searchData = [];
    this.isData = false;
    this.dataNotFound = false;
  }

  isMultiplexSelect() {
    this.isMovie = false;
    this.isMultiplex = true;
    this.searchFormGroup.reset();
    this.searchData = [];
    this.isData = false;
    this.dataNotFound = false;
  }

  searchMovie(searchString: string) {
    if (searchString.trim().length == 0) {
      this.isData = false;
      this.dataNotFound = false;
    }
    if (searchString.trim().length > 1 && searchString.trim().length < 10) {
      this.searchData = [];
      this.searchService.searchMovies(searchString).subscribe((data: SearchResult[]) => {
        this.searchData = data;
        if (this.searchData.length > 0) {
          this.isData = true;
          this.dataNotFound = false;
        } else {
          this.isData = false;
          this.dataNotFound = true;
        }
        console.log(this.searchData);
      }, err => {
        this.alertService.error(err.error.message, true);
      });
    }
  }

  searchMultiplex(searchString: string) {
    if (searchString.trim().length == 0) {
      this.isData = false;
      this.dataNotFound = false;
    }
    if (searchString.trim().length > 1 && searchString.trim().length < 10) {
      this.searchData = [];
      this.searchService.searchMultiplexes(searchString).subscribe((data: SearchResult[]) => {
        this.searchData = data;
        if (this.searchData.length > 0) {
          this.isData = true;
          this.dataNotFound = false;
        } else {
          this.isData = false;
          this.dataNotFound = true;
        }
        console.log(this.searchData);
      }, err => {
        this.alertService.error(err.error.message, true);
      })
    }
  }

  ngOnInit(): void {
  }

}
