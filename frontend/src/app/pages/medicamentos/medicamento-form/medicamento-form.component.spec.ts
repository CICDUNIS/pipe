import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicamentoFormComponent } from './medicamento-form.component';

describe('MedicamentoFormComponent', () => {
  let component: MedicamentoFormComponent;
  let fixture: ComponentFixture<MedicamentoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicamentoFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MedicamentoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
