package com.example.demo.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Anuncio;
import com.example.demo.Model.Foros;

@CacheConfig(cacheNames="cacheForos")
public interface ForosRepository extends JpaRepository<Foros, Integer> {
	
	@Cacheable(key="#root.method.name+#root.target")
	List<Foros> findAll();
	
	Optional<Foros> findById(int id);
	
	
	@CacheEvict(allEntries=true, value="cacheForos")
	Foros save(Foros foro);

	
}
