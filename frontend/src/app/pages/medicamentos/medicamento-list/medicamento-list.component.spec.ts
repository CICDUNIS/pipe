import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicamentoListComponent } from './medicamento-list.component';

describe('MedicamentoListComponent', () => {
  let component: MedicamentoListComponent;
  let fixture: ComponentFixture<MedicamentoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicamentoListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MedicamentoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
