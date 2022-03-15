import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IItemFacturaCompra, ItemFacturaCompra } from '../item-factura-compra.model';
import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

import { ItemFacturaCompraRoutingResolveService } from './item-factura-compra-routing-resolve.service';

describe('ItemFacturaCompra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ItemFacturaCompraRoutingResolveService;
  let service: ItemFacturaCompraService;
  let resultItemFacturaCompra: IItemFacturaCompra | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ItemFacturaCompraRoutingResolveService);
    service = TestBed.inject(ItemFacturaCompraService);
    resultItemFacturaCompra = undefined;
  });

  describe('resolve', () => {
    it('should return IItemFacturaCompra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaCompra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemFacturaCompra).toEqual({ id: 123 });
    });

    it('should return new IItemFacturaCompra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaCompra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultItemFacturaCompra).toEqual(new ItemFacturaCompra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ItemFacturaCompra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaCompra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemFacturaCompra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
