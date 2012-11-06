package dk.dma.arcticweb.service;

import java.util.List;

import dk.dma.arcticweb.domain.IEntity;


public class ServiceImpl implements IService {

	public static IEntity getSingleOrNull(List<? extends IEntity> list) {
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

}
