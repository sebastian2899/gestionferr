import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';

import { FacturaCompraRoutingResolveService } from './factura-compra-routing-resolve.service';

describe('FacturaCompra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FacturaCompraRoutingResolveService;
  let service: FacturaCompraService;
  let resultFacturaCompra: IFacturaCompra | undefined;

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
    routingResolveService = TestBed.inject(FacturaCompraRoutingResolveService);
    service = TestBed.inject(FacturaCompraService);
    resultFacturaCompra = undefined;
  });

  describe('resolve', () => {
    it('should return IFacturaCompra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFacturaCompra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFacturaCompra).toEqual({ id: 123 });
    });

    it('should return new IFacturaCompra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFacturaCompra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFacturaCompra).toEqual(new FacturaCompra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FacturaCompra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFacturaCompra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFacturaCompra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
