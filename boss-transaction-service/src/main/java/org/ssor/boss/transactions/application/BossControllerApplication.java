package org.ssor.boss.transactions.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.ssor.boss.transactions.repository.TransactionRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication (scanBasePackages = "org.ssor.boss.transactions.*")
@EntityScan(basePackages = "org.ssor.boss.entity")
@EnableJpaRepositories(basePackageClasses = { TransactionRepository.class })
@EnableSwagger2
public class BossControllerApplication extends WebSecurityConfigurerAdapter
{

  public static void main(String[] args)
  {
    SpringApplication.run(BossControllerApplication.class, args);
  }

}
