import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CervejasComponent} from './cervejas.component';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('CervejasComponent', () => {

  let component: CervejasComponent;
  let fixture: ComponentFixture<CervejasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CervejasComponent],
      imports: [ReactiveFormsModule, HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(CervejasComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
