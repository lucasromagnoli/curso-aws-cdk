package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.StackProps;

import java.util.Arrays;

public class AwsApp {
    public static void main(final String[] args) {
        App app = new App();
        StackProps env = StackProps.builder()
                .env(Environment.builder()
                        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                        .region(System.getenv("CDK_DEFAULT_REGION"))
                        .build())
                .build();

        VpcStack vpcStack = new VpcStack(app, "Vpc", env);

        ClusterStack clusterStack = new ClusterStack(app, "Cluster", env, vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        RdsStack rdsStack = new RdsStack(app, "Rds", env, vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        Service01Stack service01Stack = new Service01Stack(app, "Service01", env, clusterStack.getCluster());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);
        app.synth();
    }
}
