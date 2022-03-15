import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FacturaCompraDetailComponent } from './factura-compra-detail.component';

describe('FacturaCompra Management Detail Component', () => {
  let comp: FacturaCompraDetailComponent;
  let fixture: ComponentFixture<FacturaCompraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacturaCompraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ facturaCompra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FacturaCompraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FacturaCompraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load facturaCompra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.facturaCompra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
