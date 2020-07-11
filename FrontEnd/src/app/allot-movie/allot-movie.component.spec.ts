import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllotMovieComponent } from './allot-movie.component';

describe('AllotMovieComponent', () => {
  let component: AllotMovieComponent;
  let fixture: ComponentFixture<AllotMovieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllotMovieComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllotMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
