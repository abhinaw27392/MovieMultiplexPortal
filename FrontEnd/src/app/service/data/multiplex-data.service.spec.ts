import { TestBed } from '@angular/core/testing';

import { MultiplexDataService } from './multiplex-data.service';

describe('MultiplexDataService', () => {
  let service: MultiplexDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MultiplexDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
