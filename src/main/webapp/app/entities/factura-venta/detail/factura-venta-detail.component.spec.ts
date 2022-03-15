import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FacturaVentaDetailComponent } from './factura-venta-detail.component';

describe('FacturaVenta Management Detail Component', () => {
  let comp: FacturaVentaDetailComponent;
  let fixture: ComponentFixture<FacturaVentaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacturaVentaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ facturaVenta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FacturaVentaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FacturaVentaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load facturaVenta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.facturaVenta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
