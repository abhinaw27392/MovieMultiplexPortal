import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultiplexManagementComponent } from './multiplex-management.component';

describe('MultiplexManagementComponent', () => {
  let component: MultiplexManagementComponent;
  let fixture: ComponentFixture<MultiplexManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultiplexManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultiplexManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
