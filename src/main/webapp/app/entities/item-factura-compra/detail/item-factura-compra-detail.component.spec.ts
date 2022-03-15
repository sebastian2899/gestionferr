import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemFacturaCompraDetailComponent } from './item-factura-compra-detail.component';

describe('ItemFacturaCompra Management Detail Component', () => {
  let comp: ItemFacturaCompraDetailComponent;
  let fixture: ComponentFixture<ItemFacturaCompraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemFacturaCompraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ itemFacturaCompra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ItemFacturaCompraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemFacturaCompraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load itemFacturaCompra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.itemFacturaCompra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
