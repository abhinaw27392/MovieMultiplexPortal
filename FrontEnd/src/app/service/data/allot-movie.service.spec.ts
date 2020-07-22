import { TestBed } from '@angular/core/testing';

import { AllotMovieService } from './allot-movie.service';

describe('AllotMovieService', () => {
  let service: AllotMovieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AllotMovieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
